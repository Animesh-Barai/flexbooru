/*
 * Copyright (C) 2019 by onlymash <im@fiepi.me>, All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package onlymash.flexbooru.entity

data class PoolDanOne(
    var uid: Long = 0L,
    var scheme: String = "",
    var host: String = "",
    var keyword: String = "",
    val user_id: Int,
    val is_public: Boolean,
    val post_count: Int,
    val name: String,
    val updated_at: DanOneDate,
    val id: Int,
    val created_at: DanOneDate
)