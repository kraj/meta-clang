FILESEXTRAPATHS =. "${FILE_DIRNAME}/clang:"

LIC_FILES_CHKSUM = "file://llvm/LICENSE.TXT;md5=${LLVMMD5SUM} \
                    file://clang/LICENSE.TXT;md5=${CLANGMD5SUM} \
"
LICENSE = "NCSA"

BASEURI ??= "${LLVM_GIT}/llvm-project;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH}"
SRC_URI = "\
    ${BASEURI} \
    ${@'file://0000-clang-Enable-SSP-and-PIE-by-default.patch' if '${GCCPIE}' else ''} \
    file://0001-clang-driver-Use-lib-for-ldso-on-OE.patch \
    file://0002-clang-Driver-tools.cpp-Add-lssp_nonshared-on-musl.patch \
    file://0003-clang-musl-ppc-does-not-support-128-bit-long-double.patch \
    file://0004-clang-Prepend-trailing-to-sysroot.patch \
    file://0005-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch \
    file://0006-clang-Define-releative-gcc-installation-dir.patch \
    file://0007-clang-Fix-ldso-for-musl-on-x86-and-x32-architectures.patch \
    file://0008-clang-scan-view-needs-python-2.x.patch \
    file://0009-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
    file://0010-llvm-allow-env-override-of-exe-path.patch \
    file://0011-lldb-Add-lxml2-to-linker-cmdline-of-xml-is-found.patch \
    file://0012-libcxxabi-Find-libunwind-headers-when-LIBCXXABI_LIBU.patch \
    file://0013-compiler-rt-support-a-new-embedded-linux-target.patch \
    file://0014-compiler-rt-Simplify-cross-compilation.-Don-t-use-na.patch \
    file://0015-compiler-rt-Disable-tsan-on-OE-glibc.patch \
    file://0016-clang-Append-libunwind-to-compiler-rt-for-linking.patch \
    file://0017-llvm-AARCH64-Add-support-for-.dword-directives.patch \
"

# Fallback to no-PIE if not set
GCCPIE ??= ""

S = "${TMPDIR}/work-shared/llvm-project-source-${PV}-${PR}/git"
B = "${WORKDIR}/llvm-project-source-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# We need to ensure that for the shared work directory, the do_patch signatures match
# The real WORKDIR location isn't a dependency for the shared workdir.
src_patches[vardepsexclude] = "WORKDIR"
should_apply[vardepsexclude] += "PN"
