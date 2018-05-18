# Copyright (C) 2018 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

CFLAGS =+ "${TARGET_CC_ARCH} ${TOOLCHAIN_OPTIONS}"
CXXFLAGS =+ "${TARGET_CC_ARCH} ${TOOLCHAIN_OPTIONS}"

#EXTRA_ANALYZER_OPTIONS ?= "-analyze-headers"
CLANG_SCAN_ENABLED ??= "1"
#CLANG_SCAN_SERVER_IP ??= "127.0.0.1"
CLANG_SCAN_SERVER_IP ??= "10.0.0.10"
CLANG_SCAN_PORT ??= "8181"
SCAN_RESULTS_DIR ?= "${TMPDIR}/static-scan/${PN}"

scanbuild_munge_cc_cxx() {
        cc="`echo ${CC} | cut -f1 -d " "`"
        cxx="`echo ${CXX} | cut -f1 -d " "`"
        export CC="${cc}"
        export CXX="${cxx}"
}

do_configure[prefuncs] += "scanbuild_munge_cc_cxx"

do_scanbuild() {
        cc="`echo ${CC} | cut -f1 -d " "`"
        cxx="`echo ${CXX} | cut -f1 -d " "`"
        #mk="scan-build --use-cc=${cc} --use-c++=${cxx} --analyzer-target=${HOST_SYS} --html-title="${BP}" -o ${SCAN_RESULTS_DIR} ${EXTRA_ANALYZER_OPTIONS} make"
        #export MAKE="${mk}"
        export CC="${cc}"
        export CXX="${cxx}"
        scan-build --use-cc=${cc} --use-c++=${cxx} --analyzer-target=${HOST_SYS} --html-title="${BP}" -o ${SCAN_RESULTS_DIR} ${EXTRA_ANALYZER_OPTIONS} ${MAKE} ${EXTRA_OEMAKE}
}

do_scanview() {
        bbplain "================================================================"
        bbplain "Stating scan-view server at: http://${CLANG_SCAN_SERVER_IP}:${CLANG_SCAN_PORT}"
        bbplain "Use Ctrl-C to exit"
        bbplain "================================================================"
        scan-view --host ${CLANG_SCAN_SERVER_IP} --port ${CLANG_SCAN_PORT} --allow-all-hosts ${SCAN_RESULTS_DIR}/*/
}

do_scanview[depends] += "${PN}:do_scanbuild"
do_scanbuild[cleandirs] += "${SCAN_RESULTS_DIR}"
do_scanbuild[dirs] += "${B}"
do_scanview[dirs] += "${SCAN_RESULTS_DIR}"
#do_build[recrdeptask] += "do_scanbuild"

do_scanbuild[doc] = "Build and scan static analysis data using clang"
do_scanview[doc] = "Start a webserver to visualize static analysis data"

addtask scanbuild after do_configure before do_compile
addtask scanview

python () {
    # Remove scanbuild task when scanning is not enabled
    if not(d.getVar('CLANG_SCAN_ENABLED') == "1"):
        for i in ['do_scanbuild', 'do_scanview']:
            bb.build.deltask(i, d)
}
