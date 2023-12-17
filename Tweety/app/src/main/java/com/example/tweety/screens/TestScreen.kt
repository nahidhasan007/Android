package com.example.tweety.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tweety.model.PriceBreakdown

@Composable
fun ReissuePriceBreakDownFragment() {

    val priceBreakdown = PriceBreakdown("100", "100", "100", "100")
    reissuePriceBreakdown(priceBreakdown)

}

@Composable
fun reissuePriceBreakdown(
    priceBreakdown: PriceBreakdown
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Step 3: Price Information",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Card View
        // Text sections (Fare Difference, Airline Fee, Convenience Fee, Total Airfare)
        Text(
            text = "Fare Difference",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "BDT ${priceBreakdown.fareDifference?.toInt() ?: 0}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(end = 16.dp)
        )

        Text(
            text = "Airline Fee",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "BDT ${priceBreakdown.airlinesFee?.toInt() ?: 0}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(end = 16.dp)
        )

        Text(
            text = "Convenience Fee",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "BDT ${priceBreakdown.stFee?.toInt() ?: 0}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(end = 16.dp)
        )

        Text(
            text = "Total Airfare",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "BDT ${priceBreakdown.total?.toInt() ?: 0}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}
