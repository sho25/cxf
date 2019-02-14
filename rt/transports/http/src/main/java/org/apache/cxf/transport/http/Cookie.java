begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * Container for HTTP cookies used to track  * session state.  *  */
end_comment

begin_class
specifier|public
class|class
name|Cookie
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DISCARD_ATTRIBUTE
init|=
literal|"discard"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAX_AGE_ATTRIBUTE
init|=
literal|"max-age"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_ATTRIBUTE
init|=
literal|"path"
decl_stmt|;
comment|/**      * The name of this cookie      */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * The value of this cookie      */
specifier|private
name|String
name|value
decl_stmt|;
comment|/**      * The path on the server where this cookie is valid.      * Used to distinguish between identical cookies from different contexts.      */
specifier|private
name|String
name|path
decl_stmt|;
comment|/**      * The maximum age of the cookie      */
specifier|private
name|int
name|maxAge
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Create a new cookie with the supplied name/value pair      * @param name      * @param value      */
specifier|public
name|Cookie
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the name of this cookie      * @return cookie name      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
comment|/**      * Change the value of this cookie      * @param value      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of this cookie      * @return cookie value      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|value
return|;
block|}
comment|/**      * Set the path of this cookie      * @param path      */
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
comment|/**      * Get the path of this cookie      * @return cookie path      */
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|path
return|;
block|}
comment|/**      * Set the max-age of this cookie. If set to 0, it      * should be removed from the session.      * @param maxAge      */
specifier|public
name|void
name|setMaxAge
parameter_list|(
name|int
name|maxAge
parameter_list|)
block|{
name|this
operator|.
name|maxAge
operator|=
name|maxAge
expr_stmt|;
block|}
comment|/**      * Get the max-age of this cookie      * @return      */
specifier|public
name|int
name|getMaxAge
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxAge
return|;
block|}
comment|/**      *      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|(
literal|17
operator|*
name|this
operator|.
name|name
operator|.
name|hashCode
argument_list|()
operator|)
operator|+
operator|(
operator|(
name|this
operator|.
name|path
operator|!=
literal|null
operator|)
condition|?
literal|11
operator|*
name|this
operator|.
name|path
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
return|;
block|}
comment|/**      *      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Cookie
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Cookie
name|c
init|=
operator|(
name|Cookie
operator|)
name|o
decl_stmt|;
return|return
name|this
operator|.
name|name
operator|.
name|equals
argument_list|(
name|c
operator|.
name|name
argument_list|)
operator|&&
operator|(
operator|(
name|this
operator|.
name|path
operator|==
literal|null
operator|&&
name|c
operator|.
name|path
operator|==
literal|null
operator|)
operator|||
operator|(
name|this
operator|.
name|path
operator|!=
literal|null
operator|&&
name|this
operator|.
name|path
operator|.
name|equals
argument_list|(
name|c
operator|.
name|path
argument_list|)
operator|)
operator|)
return|;
block|}
comment|/**      * Convert a list of cookies into a string suitable for sending      * as a "Cookie:" header      * @return Cookie header text      */
specifier|public
name|String
name|requestCookieHeader
parameter_list|()
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"$Version=\"1\""
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
operator|.
name|append
argument_list|(
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getPath
argument_list|()
operator|!=
literal|null
operator|&&
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"; $Path="
argument_list|)
operator|.
name|append
argument_list|(
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

