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
name|util
operator|.
name|Date
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
name|DatabindingException
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
comment|/**  * AegisType for the java.sql.Date class which serializes as an xsd:date (no time  * information).  */
end_comment

begin_class
specifier|public
class|class
name|SqlDateType
extends|extends
name|DateType
block|{
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Date
name|date
init|=
operator|(
name|Date
operator|)
name|super
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|date
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|date
operator|.
name|getTime
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
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|java
operator|.
name|sql
operator|.
name|Date
name|date
init|=
operator|(
name|java
operator|.
name|sql
operator|.
name|Date
operator|)
name|object
decl_stmt|;
name|super
operator|.
name|writeObject
argument_list|(
operator|new
name|Date
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

