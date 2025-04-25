SUMMARY = "C-family abstract syntax tree XML output tool."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/CastXML/CastXML;protocol=https;branch=master"

SRCREV = "f38c024b395187814f14f77974d8f5240bb2e71f"

S = "${WORKDIR}/git"

DEPENDS = "clang"
inherit cmake pkgconfig python3native

BBCLASSEXTEND = "native nativesdk"
