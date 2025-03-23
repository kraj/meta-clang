FILESEXTRAPATHS =. "${FILE_DIRNAME}/clang:"

LIC_FILES_CHKSUM = "file://llvm/LICENSE.TXT;md5=${LLVMMD5SUM} \
                    file://clang/LICENSE.TXT;md5=${CLANGMD5SUM} \
"
LICENSE = "Apache-2.0-with-LLVM-exception"

# Snapshot
#RELEASE ?= "a69568efe6c4972e71af295c6577b3412dd57c22"
#BASEURI ?= "https://api.github.com/repos/llvm/llvm-project/tarball/${RELEASE};downloadfilename=llvm-${PV}-${RELEASE}.tar.gz"
#SOURCEDIR ?= "llvm-llvm-project-${@'${RELEASE}'[0:7]}"
#SRC_URI[sha256sum] = "aed4f66cd72867b36cc55a0588436933c3d58d3e61ca782b6bf57605d3fdcb09"

# GA Release
RELEASE ?= "${PV}"
BASEURI ?= "${LLVM_HTTP}/llvm-project/releases/download/llvmorg-${PV}/llvm-project-${PV}.src.tar.xz"
SOURCEDIR ?= "llvm-project-${PV}.src"
SRC_URI[sha256sum] = "4d5ebbd40ce1e984a650818a4bb5ae86fc70644dec2e6d54e78b4176db3332e0"

SRC_URI = "\
    ${BASEURI} \
    file://0001-libcxxabi-Find-libunwind-headers-when-LIBCXXABI_LIBU.patch \
    file://0002-compiler-rt-support-a-new-embedded-linux-target.patch \
    file://0003-compiler-rt-Simplify-cross-compilation.-Don-t-use-na.patch \
    file://0004-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
    file://0005-llvm-allow-env-override-of-exe-and-libdir-path.patch \
    file://0006-clang-driver-Check-sysroot-for-ldso-path.patch \
    file://0007-clang-Driver-tools.cpp-Add-lssp_nonshared-on-musl.patch \
    file://0008-clang-Prepend-trailing-to-sysroot.patch \
    file://0009-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch \
    file://0010-clang-Define-releative-gcc-installation-dir.patch \
    file://0011-clang-Add-lpthread-and-ldl-along-with-lunwind-for-st.patch \
    file://0012-Pass-PYTHON_EXECUTABLE-when-cross-compiling-for-nati.patch \
    file://0013-Check-for-atomic-double-intrinsics.patch \
    file://0014-cmake-Fix-configure-for-packages-using-find_package.patch \
    file://0015-clang-Fix-resource-dir-location-for-cross-toolchains.patch \
    file://0016-clang-driver-Add-dyld-prefix-when-checking-sysroot-f.patch \
    file://0017-clang-Use-python3-in-python-scripts.patch \
    file://0018-For-x86_64-set-Yocto-based-GCC-install-search-path.patch \
    file://0019-llvm-Insert-anchor-for-adding-OE-distro-vendor-names.patch \
    file://0020-compiler-rt-Do-not-use-backtrace-APIs-on-non-glibc-l.patch \
    file://0021-clang-Fix-x86-triple-for-non-debian-multiarch-linux-.patch \
    file://0022-libunwind-Added-unw_backtrace-method.patch \
    file://0023-clang-Do-not-use-install-relative-libc-headers.patch \
    file://0024-Fix-lib-paths-for-OpenEmbedded-Host.patch \
    file://0025-Correct-library-search-path-for-OpenEmbedded-Host.patch \
    file://0026-lldb-Link-with-libatomic-on-x86.patch \
    file://0027-compiler-rt-Enable-__int128-for-ppc32.patch \
    file://0028-llvm-Do-not-use-cmake-infra-to-detect-libzstd.patch \
    file://0029-compiler-rt-Fix-stat-struct-s-size-for-O32-ABI.patch \
    file://0030-compiler-rt-Undef-_TIME_BITS-along-with-_FILE_OFFSET.patch \
    file://0031-ToolChains-Gnu.cpp-ARMLibDirs-search-also-in-lib32.patch \
    file://0032-clang-llvm-Add-OE-specific-ABI-triple-for-N32-ABI.patch \
    file://0033-llvm-Add-libunwind.pc.in-and-llvm-config-scripts.patch \
    file://0034-scan-build-py-respect-LLVM_LIBDIR_SUFFIX-like-other-.patch \
    file://0035-compiler-rt-Do-not-pass-target-to-clang-compiler.patch \
    file://0036-Fix-build-on-ppc64-musl.patch \
"
# Fallback to no-PIE if not set
GCCPIE ??= ""

S = "${TMPDIR}/work-shared/llvm-project-source-${PV}-${PR}/${SOURCEDIR}"
B ?= "${WORKDIR}/llvm-project-source-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# We need to ensure that for the shared work directory, the do_patch signatures match
# The real WORKDIR location isn't a dependency for the shared workdir.
src_patches[vardepsexclude] = "WORKDIR"
should_apply[vardepsexclude] += "PN"

BPN = "clang"
