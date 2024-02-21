FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Set until https://github.com/llvm/llvm-project/issues/82541 is fixed
#DEPENDS:append:toolchain-clang = " openmp"
DEPENDS:remove:toolchain-clang:riscv32 = "openmp"
DEPENDS:remove:toolchain-clang:mipsarch = "openmp"
DEPENDS:remove:toolchain-clang:powerpc = "openmp"

# rpm needs OMP
TOOLCHAIN:riscv32 = "gcc"
TOOLCHAIN:mipsarch = "gcc"
TOOLCHAIN:powerpc = "gcc"
# Set until https://github.com/llvm/llvm-project/issues/82541 is fixed
TOOLCHAIN = "gcc"
LDFLAGS:remove = "-fuse-ld=lld"
