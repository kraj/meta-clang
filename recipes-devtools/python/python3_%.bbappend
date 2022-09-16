FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_configure:prepend:class-target:toolchain-clang() {
    # We do not need --print-multiarch with clang since it prints wrong value
    sed -i -e 's#\[MULTIARCH=$($CC --print-multiarch 2>/dev/null)\]#\[MULTIARCH=""\]#g' ${S}/configure.ac
}

