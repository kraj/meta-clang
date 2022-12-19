# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://openmp.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://openmp/LICENSE.TXT;md5=d75288d1ce0450b28b8d58a284c09c79"

LDFLAGS:append = " -fuse-ld=lld"

inherit cmake pkgconfig perlnative

DEPENDS += "elfutils libffi clang"

EXTRA_OECMAKE += "-DOPENMP_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')} \
                  -DOPENMP_STANDALONE_BUILD=ON \
                  -DCLANG_TOOL=${STAGING_BINDIR_NATIVE}/clang \
                  -DLINK_TOOL=${STAGING_BINDIR_NATIVE}/llvm-link \
                  -DOPT_TOOL=${STAGING_BINDIR_NATIVE}/opt \
                  "

OECMAKE_SOURCEPATH = "${S}/openmp"

PACKAGECONFIG ?= "ompt-tools offloading-plugin"

PACKAGECONFIG:remove:arm = "ompt-tools offloading-plugin"
PACKAGECONFIG:remove:powerpc = "ompt-tools offloading-plugin"

PACKAGECONFIG:append:mips = " no-atomics"

PACKAGECONFIG[ompt-tools] = "-DOPENMP_ENABLE_OMPT_TOOLS=ON,-DOPENMP_ENABLE_OMPT_TOOLS=OFF,"
PACKAGECONFIG[aliases] = "-DLIBOMP_INSTALL_ALIASES=ON,-DLIBOMP_INSTALL_ALIASES=OFF,"
PACKAGECONFIG[offloading-plugin] = ",,elfutils libffi,libelf libffi"
PACKAGECONFIG[no-atomics] = "-DLIBOMP_HAVE_BUILTIN_ATOMIC=OFF -DLIBOMP_LIBFLAGS='-latomic',,"

PACKAGES += "${PN}-libomptarget"
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/lib*${SOLIBSDEV}"
FILES:${PN}-libomptarget = "${libdir}/libomptarget-*.bc"
INSANE_SKIP:${PN} = "dev-so"

COMPATIBLE_HOST:mips64 = "null"
COMPATIBLE_HOST:riscv32 = "null"
COMPATIBLE_HOST:powerpc = "null"

BBCLASSEXTEND = "native nativesdk"
