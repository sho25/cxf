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
name|binding
operator|.
name|corba
operator|.
name|runtime
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
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
name|binding
operator|.
name|corba
operator|.
name|CorbaStreamable
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
name|binding
operator|.
name|corba
operator|.
name|types
operator|.
name|CorbaObjectHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TypeCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|portable
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|portable
operator|.
name|OutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaStreamableImpl
implements|implements
name|CorbaStreamable
block|{
specifier|private
name|CorbaObjectHandler
name|value
decl_stmt|;
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|int
name|mode
decl_stmt|;
specifier|private
name|TypeCode
name|typecode
decl_stmt|;
specifier|public
name|CorbaStreamableImpl
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|,
name|QName
name|elName
parameter_list|)
block|{
name|value
operator|=
name|obj
expr_stmt|;
name|name
operator|=
name|elName
expr_stmt|;
name|typecode
operator|=
name|obj
operator|.
name|getTypeCode
argument_list|()
expr_stmt|;
name|mode
operator|=
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_OUT
operator|.
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|_read
parameter_list|(
name|InputStream
name|istream
parameter_list|)
block|{
try|try
block|{
name|CorbaObjectReader
name|reader
init|=
operator|new
name|CorbaObjectReader
argument_list|(
name|istream
argument_list|)
decl_stmt|;
name|reader
operator|.
name|read
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Error reading streamable value"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|_write
parameter_list|(
name|OutputStream
name|ostream
parameter_list|)
block|{
try|try
block|{
name|CorbaObjectWriter
name|writer
init|=
operator|new
name|CorbaObjectWriter
argument_list|(
name|ostream
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Error writing streamable value"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|TypeCode
name|_type
parameter_list|()
block|{
return|return
name|typecode
return|;
block|}
specifier|public
name|CorbaObjectHandler
name|getObject
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setObject
parameter_list|(
name|CorbaObjectHandler
name|obj
parameter_list|)
block|{
name|value
operator|=
name|obj
expr_stmt|;
block|}
specifier|public
name|int
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|int
name|md
parameter_list|)
block|{
name|mode
operator|=
name|md
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
block|}
end_class

end_unit

