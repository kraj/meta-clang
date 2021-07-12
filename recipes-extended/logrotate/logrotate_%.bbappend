FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " file://logrotate-3.15.1-fno-common.patch"
