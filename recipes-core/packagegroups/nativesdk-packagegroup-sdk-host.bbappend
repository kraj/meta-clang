RDEPENDS_${PN} += "${@'nativesdk-clang' if '${CLANGSDK}' else ''}"
