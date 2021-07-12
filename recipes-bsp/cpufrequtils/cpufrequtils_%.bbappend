FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " file://cpufrequtils-008-fix-build-on-non-x86.patch"
