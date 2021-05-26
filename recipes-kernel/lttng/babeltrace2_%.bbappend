FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " file://babeltrace2-2.0.2-fix-reserved-keywords.patch"
