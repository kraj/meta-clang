# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://openmp.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://openmp/LICENSE.txt;md5=d75288d1ce0450b28b8d58a284c09c79"

inherit cmake pkgconfig perlnative

DEPENDS += "elfutils libffi"

EXTRA_OECMAKE += "-DOPENMP_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')}"

OECMAKE_SOURCEPATH = "${S}/openmp"

PACKAGECONFIG ?= "ompt-tools offloading-plugin"

PACKAGECONFIG_remove_arm = "ompt-tools offloading-plugin"
PACKAGECONFIG_remove_mipsarch = "ompt-tools offloading-plugin"
PACKAGECONFIG_remove_powerpc = "ompt-tools offloading-plugin"

PACKAGECONFIG[ompt-tools] = "-DOPENMP_ENABLE_OMPT_TOOLS=ON,-DOPENMP_ENABLE_OMPT_TOOLS=OFF,"
PACKAGECONFIG[aliases] = "-DLIBOMP_INSTALL_ALIASES=ON,-DLIBOMP_INSTALL_ALIASES=OFF,"
PACKAGECONFIG[offloading-plugin] = ",,elfutils libffi,libelf libffi"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/lib*${SOLIBSDEV}"
INSANE_SKIP_${PN} = "dev-so"

COMPATIBLE_HOST_riscv64 = "null"
COMPATIBLE_HOST_riscv32 = "null"
COMPATIBLE_HOST_mips64 = "null"

BBCLASSEXTEND = "native nativesdk"
