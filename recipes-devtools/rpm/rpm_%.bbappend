FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_append_toolchain-clang = " openmp"
DEPENDS_remove_toolchain-clang_riscv32 = "openmp"
DEPENDS_remove_toolchain-clang_mips64 = "openmp"

# rpm needs OMP
TOOLCHAIN_riscv32 = "gcc"
TOOLCHAIN_mips64 = "gcc"
