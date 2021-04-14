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
    multilib_variants = d.getVar('MULTILIB_VARIANTS').split()
    vendors_to_add = []
    for vendor in vendors.split():
        # convert -yoe into yoe
        vendor = vendor.lstrip('-')
        # generate possible multilib vendor names for yoe
        # such as yoemllib32
        vendors_to_add.extend([vendor + 'ml' + variant for variant in multilib_variants])
        # skip oe since already part of the cpp file
        if vendor != "oe":
            vendors_to_add.append(vendor)

    for vendor_to_add in vendors_to_add:
        case += '\\n    .Case("' + vendor_to_add + '", Triple::OpenEmbedded)'
        triple += ' "x86_64-' + vendor_to_add + '-linux",'

    bb.note("Adding support following TARGET_VENDOR values")
    bb.note(str(vendors_to_add))
    bb.note("in llvm/lib/Support/Triple.cpp and ${S}/clang/lib/Driver/ToolChains/Gnu.cpp")
    cmd = d.expand("sed -i 's#//CLANG_EXTRA_OE_VENDORS_TRIPLES#%s#g' ${S}/clang/lib/Driver/ToolChains/Gnu.cpp" % (triple))
    subprocess.check_output(cmd, stderr=subprocess.STDOUT, shell=True)
    cmd = d.expand("sed -i 's#//CLANG_EXTRA_OE_VENDORS_CASES#%s#g' -i ${S}/llvm/lib/Support/Triple.cpp" % (case))
    subprocess.check_output(cmd, stderr=subprocess.STDOUT, shell=True)
}

do_patch[postfuncs] += "add_distro_vendor"
