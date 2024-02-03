needle="wax synthase"
genomes_path="../genomas"
expected_sha256="48aa9f97c5d4e0dd1dd275f4eaa941e282758b9a03e8c930c6850874284ebe5c  -"

function buscar {
  TIMEFORMAT='%R seconds elapsed'
  time result="$(grep -rh "$needle" "$(readlink "$genomes_path")")" >&2
  printf "%s" "$result"
}

output=$(buscar)
output_hash="$(echo "$output" | sha256sum)"
if [ "$output_hash" == "$expected_sha256" ]
then
  echo "output hash is $output_hash"
else
  echo "unexpected hash" >&2
  exit 1
fi
