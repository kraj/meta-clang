FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# tests need compiler on target and they need the compiler which was
# used to build python
RDEPENDS:${PN}-ptest:append:toolchain-clang = " clang"

do_configure:prepend:class-target:toolchain-clang() {
    # We do not need --print-multiarch with clang since it prints wrong value
    sed -i -e 's#\[MULTIARCH=$($CC --print-multiarch 2>/dev/null)\]#\[MULTIARCH=""\]#g' ${S}/configure.ac
}

