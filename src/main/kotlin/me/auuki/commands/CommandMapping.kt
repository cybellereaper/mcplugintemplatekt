package me.auuki.commands

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandMapping(val cmd: String, val sub: String = "", val permission: String = "")
