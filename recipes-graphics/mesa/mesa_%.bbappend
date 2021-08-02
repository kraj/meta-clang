FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = " file://clang13.patch "

LDFLAGS:append:toolchain-clang = " -latomic -lm"
DEPENDS:append:toolchain-clang = " libatomic-ops"

EXTRA_OEMASON:append:toolchain-clang:x86 = " -Dasm=false"
EXTRA_OEMASON:append:toolchain-clang:x86-64 = " -Dasm=false"

export YOCTO_ALTERNATE_EXE_PATH = "${STAGING_LIBDIR}/llvm-config"

SRC_URI += "file://0001-gallium-add-missing-header-for-powf.patch \
            file://0001-gallivm-fix-build-on-llvm-12-due-to-LLVMAddConstantPropagationPass-removal.patch \
            file://0001-gallivm-add-InstSimplify-pass.patch \
            file://0001-gallium-gallivm-remove-unused-header-include-for-newer-LLVM.patch \
            "

PACKAGECONFIG[gallium-llvm] = "-Dllvm=true -Dshared-llvm=true -Ddraw-use-llvm=true,-Dllvm=false,clang clang-native elfutils"
