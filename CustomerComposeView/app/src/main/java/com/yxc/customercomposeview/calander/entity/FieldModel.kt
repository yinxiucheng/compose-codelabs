package com.yxc.customercomposeview.calander.entity

class FieldModel(tag: String, key: String, nextKey: List<Byte>) {
    val tag: String
    var key = ""
    var NextKey: List<Byte>

    init {
        this.key = key
        this.tag = tag
        NextKey = nextKey
    }
}