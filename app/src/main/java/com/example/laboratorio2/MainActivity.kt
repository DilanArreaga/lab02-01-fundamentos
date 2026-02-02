package com.example.laboratorio2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
                    CharacterSheet()
                }
            }
        }
    }
}

// ⚙️ D20 por stat para que las reglas <30 / ≥50 tengan sentido.
// Cambia a 6 si quisieras 1d6 por stat.
private const val SIDES = 20

// 🎨 Color lawngreen
private val LawnGreen = Color(0xFF7CFC00)
private val LawnGreenText = Color.Black

@Composable
fun CharacterSheet() {
    // Estados para STR, DEX, INT
    var str by rememberSaveable { mutableStateOf(0) }
    var dex by rememberSaveable { mutableStateOf(0) }
    var intell by rememberSaveable { mutableStateOf(0) }

    // Funciones de tirada
    fun rollStat(): Int = Random.nextInt(1, SIDES + 1)
    fun rollAll() {
        str = rollStat()
        dex = rollStat()
        intell = rollStat()
    }

    // Tirada inicial al entrar
    LaunchedEffect(Unit) { rollAll() }

    val total = str + dex + intell

    // Mensaje y color según validación
    val (mensaje, colorMensaje) = when {
        total < 30 -> "¡Se recomienda volver a tirar!" to Color(0xFFFF5252) // rojo
        total >= 50 -> "¡Divino!" to Color(0xFFFFD700)                  // dorado
        else -> "Aceptable" to Color(0xFF7CFC00)                            // ámbar
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estadisticas del Personaje",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Acciones generales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { rollAll() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LawnGreen,
                    contentColor = LawnGreenText
                )
            ) {
                Text("Tirar todo")
            }

            // Si prefieres que este se vea "outlined" clásico, deja OutlinedButton sin containerColor.
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { str = 0; dex = 0; intell = 0 },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = LawnGreen,    // lo convertimos en "filled" verde
                    contentColor = LawnGreenText
                ),
                border = ButtonDefaults.outlinedButtonBorder()
            ) {
                Text("Reiniciar")
            }
        }

        // Tres filas de stats con Card + Row
        StatRowCard(
            etiqueta = "STR",
            valor = str,
            onRoll = { str = rollStat() }
        )
        StatRowCard(
            etiqueta = "DEX",
            valor = dex,
            onRoll = { dex = rollStat() }
        )
        StatRowCard(
            etiqueta = "INT",
            valor = intell,
            onRoll = { intell = rollStat() }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Total
        Text(
            text = "Total: $total",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Feedback
        Text(
            text = mensaje,
            color = colorMensaje,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Fila de stat con Card + Row:
 * - Etiqueta (STR/DEX/INT)
 * - Valor grande en un contenedor
 * - Botón "Tirar" individual (lawngreen)
 */
@Composable
fun StatRowCard(
    etiqueta: String,
    valor: Int,
    onRoll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Etiqueta
            Text(
                text = etiqueta,
                modifier = Modifier.width(60.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            // Valor
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (valor == 0) "—" else valor.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }


            Button(
                onClick = onRoll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LawnGreen,
                    contentColor = LawnGreenText
                )
            ) {
                Text("Tirar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterSheetPreview() {
    LABORATORIO2Theme {
        CharacterSheet()
    }
}