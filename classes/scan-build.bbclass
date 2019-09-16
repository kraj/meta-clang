# Copyright (C) 2018 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

EXTRA_ANALYZER_OPTIONS += "-analyze-headers"
EXTRA_ANALYZER_OPTIONS += "--force-analyze-debug-code"
SCAN_BUILD ??= "1"
SCAN_BUILD_class-native = ""
SCAN_BUILD_class-nativesdk = ""
SCAN_BUILD_class-cross = ""
SCAN_BUILD_class-crosssdk = ""
SCAN_BUILD_class-cross-canadian = ""

#CLANG_SCAN_SERVER_IP ??= "127.0.0.1"
CLANG_SCAN_SERVER_IP ??= "10.0.0.10"
CLANG_SCAN_PORT ??= "8181"
SCAN_RESULTS_DIR ?= "${TMPDIR}/static-scan/${PN}"

CCSCAN ?= "${HOST_PREFIX}${TOOLCHAIN}"
CXXSCAN_toolchain-clang ?= "${HOST_PREFIX}clang++"
CXXSCAN_toolchain-gcc ?= "${HOST_PREFIX}g++"

do_scanbuild() {
        rm -rf ${SCAN_RESULTS_DIR}
        scan-build --use-cc ${CCSCAN} --use-c++ ${CXXSCAN} --analyzer-target ${HOST_SYS} --html-title ${BP} -o ${SCAN_RESULTS_DIR} ${EXTRA_ANALYZER_OPTIONS} ${MAKE} ${PARALLEL_MAKE} ${EXTRA_OEMAKE}
}

do_scanview() {
        bbplain "================================================================"
        bbplain "Stating scan-view server at: http://${CLANG_SCAN_SERVER_IP}:${CLANG_SCAN_PORT}"
        bbplain "Use Ctrl-C to exit"
        bbplain "================================================================"
        scan-view --host ${CLANG_SCAN_SERVER_IP} --port ${CLANG_SCAN_PORT} --allow-all-hosts ${SCAN_RESULTS_DIR}/*/
}

do_scanview[depends] += "${PN}:do_scanbuild"
do_scanbuild[depends] += "clang-native:do_populate_sysroot"
#do_scanbuild[cleandirs] += "${SCAN_RESULTS_DIR}"
do_scanbuild[dirs] += "${B}"
do_scanview[dirs] += "${SCAN_RESULTS_DIR}"
#do_build[recrdeptask] += "do_scanbuild"

do_scanbuild[doc] = "Build and scan static analysis data using clang"
do_scanview[doc] = "Start a webserver to visualize static analysis data"

addtask scanbuild after do_configure before do_compile
addtask scanview after do_scanbuild
python () {
    # Remove scanbuild task when scanning is not enabled or recipe does not have do_configure
    if not(d.getVar('SCAN_BUILD') == "1") or not(d.getVar('TOOLCHAIN') == "clang") or oe.utils.inherits(d, 'allarch'):
        for i in ['do_scanbuild', 'do_scanview']:
            bb.build.deltask(i, d)
    else:
        cflags = d.getVar('CFLAGS', False) + d.getVar('TARGET_CC_ARCH', False) + d.getVar('TOOLCHAIN_OPTIONS', False)
        cxxflags = d.getVar('CXXFLAGS', False) + d.getVar('TARGET_CC_ARCH', False) + d.getVar('TOOLCHAIN_OPTIONS', False)
        d.setVar('CFLAGS', cflags)
        d.setVar('CXXFLAGS', cxxflags)
        if oe.utils.inherits(d, 'autotools'):
            cachedvar = d.getVar('CACHED_CONFIGUREVARS', False)
            cachedvar = cachedvar + " scan-build " + " --analyzer-target " + d.getVar('HOST_SYS', False) + " --use-cc " + d.getVar('CCSCAN', False) + " --use-c++ " + d.getVar('CXXSCAN', False)
            d.setVar('CACHED_CONFIGUREVARS', cachedvar)
}
