SUMMARY = "C-family abstract syntax tree XML output tool."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/CastXML/CastXML;protocol=https;branch=master"

SRCREV = "1a460924d456d9983082d615e0df5f797fdeb635"

S = "${WORKDIR}/git"

DEPENDS = "clang"
inherit cmake cmake-native pkgconfig python3native

BBCLASSEXTEND = "native nativesdk"
