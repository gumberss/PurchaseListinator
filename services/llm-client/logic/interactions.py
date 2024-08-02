def is_final_state(interaction):
    return interaction.status == 'completed' or interaction.status == 'failed'

def is_timed_out(interaction, date_epoch: int):
    return interaction.request_date + interaction.timeout < date_epoch
