import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pax.ecr.app.AdminAction
import com.pax.ecr.app.R

@Composable
fun AdminMenu(
    modifier: Modifier = Modifier,
    actionHandler: (AdminAction) -> Unit,
) {
    Column(
        modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(3.dp)),
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Menu",
            style = MaterialTheme.typography.titleLarge,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp),
        ) {
            AdminMenuButton("Admin Menu") { actionHandler(AdminAction.OPEN_ADMIN_MENU) }
            AdminMenuButton("Show payment app") { actionHandler(AdminAction.MOVE_TO_FRONT) }
            AdminMenuButton("Temporarily show payment app") { actionHandler(AdminAction.TEMPORARY_SHOW) }
            AdminMenuButton("Configuration") { actionHandler(AdminAction.OPEN_CONFIG_MENU) }
            AdminMenuButton("Go to mode selector") { actionHandler(AdminAction.MODE_SELECTOR) }
        }
    }
}

@Composable
private fun AdminMenuButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .height(48.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.full_forward_arrow),
            contentDescription = "Full forward arrow",
            modifier = Modifier.size(16.dp),
        )
    }
}
