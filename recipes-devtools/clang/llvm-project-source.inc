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

# space separated list of additional distro vendor values we want to support e.g.
# "yoe webos" or "-yoe -webos" '-' is optional
CLANG_EXTRA_OE_VENDORS ?= "${TARGET_VENDOR}"

python add_distro_vendor() {
    import subprocess
    case = ""
    triple = ""
    vendors = d.getVar('CLANG_EXTRA_OE_VENDORS')
    for vendor in vendors.split():
        # convert -yoe into yoe
        vendor = vendor.lstrip('-')
        if vendor == "oe":
            continue
        case += '\\n    .Case("' + vendor + '", Triple::OpenEmbedded)'
        triple += ' "x86_64-' + vendor + '-linux",'
    cmd = d.expand("sed -i 's#//CLANG_EXTRA_OE_VENDORS_TRIPLES#%s#g' ${S}/clang/lib/Driver/ToolChains/Gnu.cpp" % (triple))
    subprocess.check_output(cmd, stderr=subprocess.STDOUT, shell=True)
    cmd = d.expand("sed -i 's#//CLANG_EXTRA_OE_VENDORS_CASES#%s#g' -i ${S}/llvm/lib/Support/Triple.cpp" % (case))
    subprocess.check_output(cmd, stderr=subprocess.STDOUT, shell=True)
}

do_patch[postfuncs] += "add_distro_vendor"
