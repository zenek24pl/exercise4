package wdsr.exercise4.receiver;

import wdsr.exercise4.PriceAlert;
import wdsr.exercise4.VolumeAlert;

public interface AlertService {
	void processPriceAlert(PriceAlert priceAlert);
	void processVolumeAlert(VolumeAlert volumeAlert);
}
