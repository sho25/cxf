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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|common
operator|.
name|JoseException
import|;
end_import

begin_class
specifier|public
class|class
name|JwsException
extends|extends
name|JoseException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4118589816228511524L
decl_stmt|;
specifier|private
specifier|final
name|Error
name|status
decl_stmt|;
specifier|public
name|JwsException
parameter_list|(
name|Error
name|status
parameter_list|)
block|{
name|this
argument_list|(
name|status
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsException
parameter_list|(
name|Error
name|status
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|status
operator|!=
literal|null
condition|?
name|status
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
specifier|public
name|Error
name|getError
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
enum|enum
name|Error
block|{
name|NO_PROVIDER
block|,
name|NO_VERIFIER
block|,
name|NO_INIT_PROPERTIES
block|,
name|ALGORITHM_NOT_SET
block|,
name|INVALID_ALGORITHM
block|,
name|INVALID_KEY
block|,
name|SIGNATURE_FAILURE
block|,
name|INVALID_SIGNATURE
block|,
name|INVALID_COMPACT_JWS
block|,
name|INVALID_JSON_JWS
block|}
block|}
end_class

end_unit

