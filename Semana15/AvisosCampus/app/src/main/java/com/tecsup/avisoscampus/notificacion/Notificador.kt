package com.tecsup.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

object Notificador {
    const val CANAL = "avisos"
    const val URGENTES = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT))
            manager.createNotificationChannel(NotificationChannel(URGENTES, "Urgentes", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Recordatorios que requieren atención"
            })
        }
    }

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        val manager = NotificationManagerCompat.from(context)
        if (!tienePermiso(context) || !manager.areNotificationsEnabled()) return false
        val urgente = aviso.origen == Origen.RECORDATORIO
        val canal = if (urgente) URGENTES else CANAL
        if (Build.VERSION.SDK_INT >= 26 && context.getSystemService(NotificationManager::class.java).getNotificationChannel(canal)?.importance == NotificationManager.IMPORTANCE_NONE) return false
        val pendiente = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }, PendingIntent.FLAG_IMMUTABLE)
        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))
            .setPriority(if (urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendiente)
            .setAutoCancel(true)
            .build()
        return try { manager.notify(aviso.id, notificacion); true } catch (_: SecurityException) { false }
    }
}
