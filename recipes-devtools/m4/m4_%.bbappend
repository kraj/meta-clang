FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://disable_builtin_mul_overflow.patch"
