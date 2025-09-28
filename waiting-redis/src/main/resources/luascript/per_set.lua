redis.call('SET', KEYS[1], ARGV[1])
redis.call('SET', KEYS[2], ARGV[2])
redis.call('expire', KEYS[1], ARGV[3])
redis.call('expire', KEYS[2], ARGV[3])

return 1