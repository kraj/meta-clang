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
