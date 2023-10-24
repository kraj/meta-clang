FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_configure:prepend:toolchain-clang () {
    if ${@bb.utils.contains('TC_CXX_RUNTIME', 'llvm', 'true', 'false', d)}; then
        sed -i -e "s|-stdlib=libstdc++|-stdlib=libc++|g" ${S}/configure.ac
    fi
}
