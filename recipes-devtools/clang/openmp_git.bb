# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://openmp.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

RPROVIDES_${PN} += "libgomp"
RPROVIDES_${PN}-dev += "libgomp-dev"

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://openmp/LICENSE.txt;md5=d75288d1ce0450b28b8d58a284c09c79"

inherit cmake pkgconfig perlnative

OECMAKE_SOURCEPATH = "${S}/openmp"

PACKAGECONFIG ?= "ompt-tools"
PACKAGECONFIG_remove_arm = "ompt-tools"
PACKAGECONFIG_remove_mipsarch = "ompt-tools"
PACKAGECONFIG_remove_powerpc = "ompt-tools"

PACKAGECONFIG[ompt-tools] = "-DOPENMP_ENABLE_OMPT_TOOLS=ON,-DOPENMP_ENABLE_OMPT_TOOLS=OFF,"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/lib*${SOLIBSDEV}"
INSANE_SKIP_${PN} = "dev-so"

COMPATIBLE_HOST_riscv64 = "null"
COMPATIBLE_HOST_riscv32 = "null"
COMPATIBLE_HOST_mips64 = "null"
