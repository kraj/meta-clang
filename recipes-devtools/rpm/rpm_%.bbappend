FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS:append:toolchain-clang = " openmp"
DEPENDS:remove:toolchain-clang:riscv32 = "openmp"
DEPENDS:remove:toolchain-clang:mipsarch = "openmp"
DEPENDS:remove:toolchain-clang:powerpc = "openmp"

# rpm needs OMP
TOOLCHAIN:riscv32 = "gcc"
TOOLCHAIN:mipsarch = "gcc"
TOOLCHAIN:powerpc = "gcc"
