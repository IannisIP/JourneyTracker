package journey.project.services

import journey.project.data.TraseuCuPuncte

interface Communicator {
    fun passDataCom(traseu: TraseuCuPuncte)
}