FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

do_configure_prepend_toolchain-clang () {
    if ${@bb.utils.contains('RUNTIME', 'llvm', 'true', 'false', d)}; then
        sed -i -e "s|-stdlib=libstdc++|-stdlib=libc++|g" ${S}/configure.ac
    fi
}
