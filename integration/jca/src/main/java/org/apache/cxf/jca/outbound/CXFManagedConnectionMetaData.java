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
name|jca
operator|.
name|outbound
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ManagedConnectionMetaData
import|;
end_import

begin_comment
comment|/**  * CXF Managed Connection MetaData  */
end_comment

begin_class
specifier|public
class|class
name|CXFManagedConnectionMetaData
implements|implements
name|ManagedConnectionMetaData
block|{
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**      * @param userName      */
specifier|public
name|CXFManagedConnectionMetaData
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|String
name|getEISProductName
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
literal|"Apache CXF"
return|;
block|}
specifier|public
name|String
name|getEISProductVersion
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
literal|"2.0"
return|;
block|}
comment|/*      * Don't have a hard limit      */
specifier|public
name|int
name|getMaxConnections
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
name|userName
return|;
block|}
block|}
end_class

end_unit

