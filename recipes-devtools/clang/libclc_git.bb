DESCRIPTION = "LLVM based OpenCL runtime support library"
HOMEPAGE = "http://libclc.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://libclc/LICENSE.TXT;md5=7cc795f6cbb2d801d84336b83c8017db"

inherit cmake pkgconfig python3native qemu

DEPENDS += "qemu-native clang spirv-tools spirv-llvm-translator spirv-llvm-translator-native ncurses"

OECMAKE_SOURCEPATH = "${S}/libclc"

EXTRA_OECMAKE += " \
				-DCMAKE_CROSSCOMPILING_EMULATOR=${WORKDIR}/qemuwrapper \
				-Dclc_comp_in:FILEPATH=${OECMAKE_SOURCEPATH}/cmake/CMakeCLCCompiler.cmake.in \
				-Dll_comp_in:FILEPATH=${OECMAKE_SOURCEPATH}/cmake/CMakeLLAsmCompiler.cmake.in \
                                -DCMAKE_LLAsm_FLAGS=-cl-no-stdinc -DCMAKE_CLC_FLAGS=-cl-no-stdinc \
			"

do_configure_prepend () {
	# Write out a qemu wrapper that will be used by cmake
	# so that it can run target helper binaries through that.
	qemu_binary="${@qemu_wrapper_cmdline(d, d.getVar('STAGING_DIR_HOST'), [d.expand('${STAGING_DIR_HOST}${libdir}'),d.expand('${STAGING_DIR_HOST}${base_libdir}')])}"
	cat > ${WORKDIR}/qemuwrapper << EOF
#!/bin/sh
$qemu_binary "\$@"
EOF
	chmod +x ${WORKDIR}/qemuwrapper
}

FILES_${PN} += "${datadir}/clc"

BBCLASSEXTEND = "native nativesdk"
