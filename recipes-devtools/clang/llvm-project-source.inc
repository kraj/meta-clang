deltask do_configure
deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_populate_lic
RM_WORK_EXCLUDE += "${PN}"

inherit nopackages

PN = "llvm-project-source-${PV}"

WORKDIR = "${TMPDIR}/work-shared/llvm-project-source-${PV}-${PR}"
SSTATE_SWSPEC = "sstate:llvm-project-source::${PV}:${PR}::${SSTATE_VERSION}:"

STAMP = "${STAMPS_DIR}/work-shared/llvm-project-source-${PV}-${PR}"
STAMPCLEAN = "${STAMPS_DIR}/work-shared/llvm-project-source-${PV}-*"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = ""
PACKAGES = ""

# additional TARGET_VENDOR values we want to support
CLANG_EXTRA_OE_VENDORS ?= "${TARGET_VENDOR}"

add_more_target_vendors() {
    local cases="" triples=""
    for dash_vendor in ${CLANG_EXTRA_OE_VENDORS}; do
        vendor=`echo $dash_vendor | sed 's/^-//g'`
        cases="$cases.Case(\"$vendor\", Triple::OpenEmbedded)"
        triples="$triples\"x86_64-$vendor-linux\","
    done
    bbnote "Adding support following TARGET_VENDOR values: ${CLANG_EXTRA_OE_VENDORS} in ${S}/llvm/lib/Support/Triple.cpp and ${S}/clang/lib/Driver/ToolChains/Gnu.cpp"
    sed "s#//CLANG_EXTRA_OE_VENDORS_CASES#$cases#g" -i ${S}/llvm/lib/Support/Triple.cpp
    sed "s#//CLANG_EXTRA_OE_VENDORS_TRIPLES#$triples#g" -i ${S}/clang/lib/Driver/ToolChains/Gnu.cpp
}

do_patch[postfuncs] += "add_more_target_vendors"
