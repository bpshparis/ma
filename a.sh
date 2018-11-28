#!/bin/sh

output=vcap.json

echo '{}' | tee $output

services=$(ibmcloud service list | awk 'NR>6 {print "{\""$2"\":[{\"credentials\":null,\"name\":\""$1"\"}]}"}')

for service in $(echo $services)
	do 
		jq --argjson value $service '. += $value' $output | sponge $output; 
	done

for svc in $(ibmcloud service list | awk 'NR>6 {print $1 ":" $2}')
	do
		echo $svc;
		label=$(echo $svc | cut -d':' -f2);
		name=$(echo $svc | cut -d':' -f1);
		echo $label;
		echo $name;
		
		key=$(ibmcloud service key-show $name user0 | awk 'NR>4');
		
		echo $key
		
		jq \
		--arg name $name \
		--arg label $label \
		--argjson clef "$key" \
		'if (.["'$label'"][].name==$name) then .["'$label'"][].credentials = $clef else . end' $output | sponge $output;
	done
	
jq . $output
	
exit 0;
