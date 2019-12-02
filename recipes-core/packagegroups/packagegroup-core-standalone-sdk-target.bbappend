RRECOMMENDS_${PN} += "${@bb.utils.contains('CLANGSDK', '1', 'libcxx-dev libcxx-staticdev compiler-rt-dev compiler-rt-staticdev', '', d)}"
