package com.example.laboratorio2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laboratorio2.ui.theme.LABORATORIO2Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LABORATORIO2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceApp()
                }
            }
        }
    }
}

// Cambia a 6 si quieres dados 1–6
private const val SIDES = 20

@Composable
fun DiceApp() {
    var d1 by rememberSaveable { mutableStateOf(0) }
    var d2 by rememberSaveable { mutableStateOf(0) }
    var d3 by rememberSaveable { mutableStateOf(0) }

    val total = d1 + d2 + d3

    val (message, color) = when {
        total < 30 -> "Malo" to Color(0xFFFF5252)      // rojo
        total >= 50 -> "¡Muy bueno!" to Color(0xFF00E676) // verde
        else -> "Regular" to Color(0xFFFFD54F)         // ámbar
    }

    fun roll() {
        d1 = Random.nextInt(1, SIDES + 1)
        d2 = Random.nextInt(1, SIDES + 1)
        d3 = Random.nextInt(1, SIDES + 1)
    }

    // Tirada inicial cuando se compone la pantalla
    LaunchedEffect(Unit) { roll() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Tira 3 dados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DieFace(value = d1, modifier = Modifier.weight(1f))
            DieFace(value = d2, modifier = Modifier.weight(1f))
            DieFace(value = d3, modifier = Modifier.weight(1f))
        }

        Button(
            onClick = { roll() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tirar dados")
        }

        Text(
            "Total: $total",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Mensaje: $message",
            color = color,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DieFace(value: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (value == 0) "—" else value.toString(),
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiceAppPreview() {
    LABORATORIO2Theme {
        DiceApp()
    }
}