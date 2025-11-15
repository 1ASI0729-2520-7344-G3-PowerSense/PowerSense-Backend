package com.powersense.analytics.dashboard.application.internal.queryservices;

import com.powersense.analytics.dashboard.application.model.DashboardTipResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardTipsQueryServiceImpl {

	public List<DashboardTipResponse> getTips() {
		return Arrays.asList(
				new DashboardTipResponse("tip-1", "Apaga las luces que no uses", "lighting"),
				new DashboardTipResponse("tip-2", "Desconecta cargadores cuando no estén en uso", "standby"),
				new DashboardTipResponse("tip-3", "Usa electrodomésticos en horario valle", "scheduling"),
				new DashboardTipResponse("tip-4", "Programa el aire acondicionado para apagarse por la noche", "ac"),
				new DashboardTipResponse("tip-5", "Revisa el consumo de dispositivos antiguos", "efficiency")
		);
	}
}
