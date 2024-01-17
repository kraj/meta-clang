
# Remove tests when using clang since, clang generates debug info
# for asm files too, like freebsd outputs but we are on linux
# and valgrind tests think its always using gcc on linux
do_install_ptest:append:toolchain-clang () {
    if [ "${@bb.utils.contains('TC_CXX_RUNTIME', 'llvm', 'True', 'False', d)}" ]
    then
        rm ${D}${PTEST_PATH}/memcheck/tests/gone_abrt_xml.vgtest
        rm ${D}${PTEST_PATH}/memcheck/tests/threadname_xml.vgtest
        rm ${D}${PTEST_PATH}/drd/tests/annotate_barrier_xml.vgtest
        rm ${D}${PTEST_PATH}/none/tests/fdleak_cmsg.vgtest
    fi
}
