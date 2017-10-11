package com.xbongbong.docall.base

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc:
 * Date: 2017-10-11
 * Time: 09:52
 */
interface BaseView {
    fun isNetworkAvailable(): Boolean
    fun isNetworkAvailable(withTip: Boolean): Boolean
    fun showToast(msg: Int)
    fun showToast(msg: String)
    fun showSuccessToast(msg: Int)
    fun showSuccessToast(msg: String)
    fun showErrorToast(msg: Int)
    fun showErrorToast(msg: String)
}