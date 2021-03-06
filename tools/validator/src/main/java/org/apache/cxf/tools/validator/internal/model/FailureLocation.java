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
name|tools
operator|.
name|validator
operator|.
name|internal
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|Location
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|FailureLocation
block|{
specifier|private
name|Location
name|location
decl_stmt|;
specifier|private
name|String
name|documentURI
decl_stmt|;
specifier|public
name|FailureLocation
parameter_list|(
name|Location
name|loc
parameter_list|,
name|String
name|docURI
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|loc
expr_stmt|;
name|this
operator|.
name|documentURI
operator|=
name|docURI
expr_stmt|;
if|if
condition|(
name|documentURI
operator|==
literal|null
condition|)
block|{
name|documentURI
operator|=
name|loc
operator|.
name|getSystemId
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Location
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
specifier|public
name|void
name|setLocation
parameter_list|(
specifier|final
name|Location
name|newLocation
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|newLocation
expr_stmt|;
block|}
specifier|public
name|String
name|getDocumentURI
parameter_list|()
block|{
return|return
name|documentURI
return|;
block|}
specifier|public
name|void
name|setDocumentURI
parameter_list|(
specifier|final
name|String
name|newDocumentURI
parameter_list|)
block|{
name|this
operator|.
name|documentURI
operator|=
name|newDocumentURI
expr_stmt|;
block|}
block|}
end_class

end_unit

