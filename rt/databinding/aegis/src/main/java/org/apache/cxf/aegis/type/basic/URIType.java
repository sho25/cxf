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
name|aegis
operator|.
name|type
operator|.
name|basic
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|AegisType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
import|;
end_import

begin_comment
comment|/**  *<code>AegisType</code> for a<code>URI</code>  */
end_comment

begin_class
specifier|public
class|class
name|URIType
extends|extends
name|AegisType
block|{
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
specifier|final
name|MessageReader
name|reader
parameter_list|,
specifier|final
name|Context
name|context
parameter_list|)
block|{
specifier|final
name|String
name|value
init|=
name|reader
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
literal|null
operator|==
name|value
condition|?
literal|null
else|:
name|URI
operator|.
name|create
argument_list|(
name|value
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
specifier|final
name|Object
name|object
parameter_list|,
specifier|final
name|MessageWriter
name|writer
parameter_list|,
specifier|final
name|Context
name|context
parameter_list|)
block|{
name|writer
operator|.
name|writeValue
argument_list|(
operator|(
operator|(
name|URI
operator|)
name|object
operator|)
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

