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
name|systest
operator|.
name|mtom_feature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|MTOM
import|;
end_import

begin_class
annotation|@
name|MTOM
specifier|public
class|class
name|HelloImpl
implements|implements
name|Hello
block|{
specifier|public
name|void
name|detail
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
parameter_list|,
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
parameter_list|)
block|{
comment|// echo through Holder
block|}
specifier|public
name|void
name|echoData
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|data
parameter_list|)
block|{
comment|// echo through Holder
block|}
block|}
end_class

end_unit

