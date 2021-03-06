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
name|throttling
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ThrottleResponse
block|{
specifier|protected
name|long
name|delay
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|int
name|responseCode
init|=
operator|-
literal|1
decl_stmt|;
specifier|protected
name|String
name|errorMessage
decl_stmt|;
specifier|public
name|ThrottleResponse
parameter_list|()
block|{      }
specifier|public
name|ThrottleResponse
parameter_list|(
name|int
name|responceCode
parameter_list|)
block|{
name|this
operator|.
name|responseCode
operator|=
name|responceCode
expr_stmt|;
block|}
specifier|public
name|ThrottleResponse
parameter_list|(
name|int
name|responceCode
parameter_list|,
name|long
name|delay
parameter_list|)
block|{
name|this
argument_list|(
name|responceCode
argument_list|)
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getResponseHeaders
parameter_list|()
block|{
return|return
name|responseHeaders
return|;
block|}
comment|/**      * Add headers to the response.  Typically, this would be things like X-RateLimit-Limit headers      * @return      */
specifier|public
name|ThrottleResponse
name|addResponseHeader
parameter_list|(
name|String
name|header
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|responseHeaders
operator|.
name|put
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|int
name|getResponseCode
parameter_list|()
block|{
return|return
name|responseCode
return|;
block|}
specifier|public
name|String
name|getErrorMessage
parameter_list|()
block|{
return|return
name|errorMessage
return|;
block|}
specifier|public
name|ThrottleResponse
name|setResponseCode
parameter_list|(
name|int
name|rc
parameter_list|)
block|{
return|return
name|setResponseCode
argument_list|(
name|rc
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|ThrottleResponse
name|setResponseCode
parameter_list|(
name|int
name|rc
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|this
operator|.
name|responseCode
operator|=
name|rc
expr_stmt|;
name|this
operator|.
name|errorMessage
operator|=
name|msg
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Delay processing for specified milliseconds.      * Should be "small" to prevent the client from timing out unless the client request is      * aborted with the HTTP error code.      * @return      */
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
specifier|public
name|ThrottleResponse
name|setDelay
parameter_list|(
name|long
name|d
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|d
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

