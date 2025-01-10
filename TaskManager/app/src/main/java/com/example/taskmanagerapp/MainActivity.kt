package com.example.taskmanagerapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagerapp.ui.theme.TaskManagerAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskScreen()
            }
        }
    }
}

enum class TaskPriority{BAIXA, MEDIA, ALTA}
enum class TaskCategory{TRABALHO, CASA, ESTUDOS}

data class Task(
    val name: String,
    val isCompleted: Boolean = false,
    val category: TaskCategory = TaskCategory.CASA,
    val priority: TaskPriority = TaskPriority.MEDIA
)

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreUtils{
    private val TASKS_KEY = stringPreferencesKey("tasks")
    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    suspend fun saveTasks(context: Context, tasks: String){
        context.dataStore.edit { prefs ->
            prefs[TASKS_KEY] = tasks
        }
    }

    fun readTasks(context: Context): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[TASKS_KEY] ?: ""
    }

    suspend fun saveTheme(context: Context, isDark: Boolean){
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }

    fun readTheme(context: Context): Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: false
    }
}

class TaskViewModel(context: Context): ViewModel(){
    private val _tasks = MutableStateFlow(
        listOf(
            Task("Reunião importante", false, TaskCategory.TRABALHO, TaskPriority.ALTA),
            Task("Estudar JetpackCompose", false, TaskCategory.ESTUDOS, TaskPriority.MEDIA),
            Task("Limpar a casa", false, TaskCategory.CASA, TaskPriority.BAIXA),

        )
    )

    val tasks: StateFlow<List<Task>> = _tasks

    private var lastDeletedTask: Task? = null

    fun removeTask(task: Task){
        lastDeletedTask = task
        _tasks.value = _tasks.value - task
    }

    fun undoDelete(){
        lastDeletedTask?.let {
            _tasks.value = _tasks.value + it
            lastDeletedTask = null
        }
    }

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    val themeFlow: Flow<Boolean> = DataStoreUtils.readTheme(context)
    private val _isDarkTheme = MutableStateFlow(false)

    init {
        viewModelScope.launch{
            themeFlow.collect{_isDarkTheme.value = it}
            updateProgress()
        }
    }

    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
        updateProgress()
    }


    fun toggleTaskCompletion(task: Task){
        _tasks.value = _tasks.value.map {
            if(it == task) it.copy(isCompleted = !it.isCompleted) else it
        }
        updateProgress()
    }

    private fun updateProgress(){
        val completed = _tasks.value.count{it.isCompleted}
        _progress.value = if(_tasks.value.isNotEmpty()) completed.toFloat() / _tasks.value.size else 0f
    }

    fun toggleTheme(context: Context){
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            DataStoreUtils.saveTheme(context, newTheme)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel = TaskViewModel(LocalContext.current)){
    val tasks by viewModel.tasks.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    MaterialTheme (
        colorScheme = if(isDarkTheme) darkColorScheme() else lightColorScheme()
    ){
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {Text("Gerenciador de Tarefas")},
                    actions = {
                        IconButton(onClick = {viewModel.toggleTheme(context)}) {
                            Icon(Icons.Default.BrightnessHigh, contentDescription = "Alternar Tema")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EE))
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Progresso nas tarefas")
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(bottom = 8.dp)
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(tasks){ task ->
                        TaskItem(
                            task = task,
                            onToggleCompletion = {viewModel.toggleTaskCompletion(task)},
                            onDelete = {
                                viewModel.removeTask(task)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Tarefa removida",
                                        actionLabel = "Desfazer",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed){
                                        viewModel.undoDelete()
                                    }
                                }
                            }
                        )
                    }
                }
                AddTaskSection(onAddTask = { name, category, priority ->
                    viewModel.addTask(Task(name, false, category, priority))
                })
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggleCompletion: () -> Unit, onDelete: () -> Unit) {
    val scale by animateFloatAsState(if (task.isCompleted) 1.05f else 1f)
    val backgroundColor = when (task.priority) {
        TaskPriority.BAIXA -> Color(0xFFC8E6C9)
        TaskPriority.MEDIA -> Color(0xFFFFF59D)
        TaskPriority.ALTA -> Color(0xFFFFCDD2)
    }

    AnimatedVisibility(visible = true) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, _ ->
                        onDelete()
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggleCompletion() })
            Text(
                text = task.name,
                modifier = Modifier.weight(1f),
                color = if (task.isCompleted) Color.Gray else Color.Black
            )
        }
    }
}

@Composable
fun AddTaskSection(onAddTask: (String, TaskCategory, TaskPriority) -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.CASA) }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIA) }

    Column {
        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Nova tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DropdownMenuBox("Categoria", TaskCategory.values().map { it.name }) {
                selectedCategory = TaskCategory.valueOf(it)
            }
            DropdownMenuBox("Prioridade", TaskPriority.values().map { it.name }) {
                selectedPriority = TaskPriority.valueOf(it)
            }
        }
        Button(
            onClick = {
                if (taskName.isNotBlank()) {
                    onAddTask(taskName, selectedCategory, selectedPriority)
                    taskName = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Adicionar Tarefa")
        }
    }
}

@Composable
fun DropdownMenuBox(label: String, options: List<String>, onSelection: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    Box(modifier = Modifier.padding(4.dp)) {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selectedOption")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        onSelection(option)
                        expanded = false
                    }
                )
            }
        }
    }


    @Composable
    fun TaskItem(task: Task, onToggleCompletion: () -> Unit, onDelete: () -> Unit) {
        val scale by animateFloatAsState(if (task.isCompleted) 1.05f else 1f)
        val backgroundColor = when (task.priority) {
            TaskPriority.BAIXA -> Color(0xFFC8E6C9)
            TaskPriority.MEDIA -> Color(0xFFFFF59D)
            TaskPriority.ALTA -> Color(0xFFFFCDD2)
        }

        AnimatedVisibility(visible = true) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, _ ->
                            onDelete()
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = task.isCompleted, onCheckedChange = { onToggleCompletion() })
                Text(
                    text = task.name,
                    modifier = Modifier.weight(1f),
                    color = if (task.isCompleted) Color.Gray else Color.Black
                )
            }
        }
    }
}