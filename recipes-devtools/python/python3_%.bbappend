FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# tests need compiler on target and they need the compiler which was
# used to build python
RDEPENDS:${PN}-ptest:append:toolchain-clang = " clang"

do_configure:prepend:toolchain-clang() {
    # --print-multiarch with clang prints wrong value as this is gcc specific option anyway to omit it
    # OE does not depend upon this option anyway
    sed -i -e 's#\[MULTIARCH=$($CC --print-multiarch 2>/dev/null)\]#\[MULTIARCH=""\]#g' ${S}/configure.ac
}

