import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [banks, setBanks] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingBank, setEditingBank] = useState(null)
  const [formData, setFormData] = useState({
    code: '',
    name: '',
    country: '',
    active: true
  })

  // Fetch all banks
  const fetchBanks = async () => {
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/api/banks')
      if (!response.ok) throw new Error('Failed to fetch banks')
      const data = await response.json()
      setBanks(data)
      setError('')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // Test internal query
  const testInternalQuery = async () => {
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/api/banks/internal-query')
      if (!response.ok) throw new Error('Failed to test internal query')
      const data = await response.json()
      setBanks(data)
      setError('')
      alert('Internal query successful! Check console for details.')
      console.log('Internal query result:', data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // Create or update bank
  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    
    try {
      const url = editingBank 
        ? `http://localhost:8080/api/banks/${editingBank.id}`
        : 'http://localhost:8080/api/banks'
      
      const method = editingBank ? 'PUT' : 'POST'
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Failed to save bank')
      }

      await fetchBanks()
      resetForm()
      setError('')
      alert(editingBank ? 'Bank updated successfully!' : 'Bank created successfully!')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // Delete bank
  const deleteBank = async (id) => {
    if (!confirm('Are you sure you want to delete this bank?')) return
    
    setLoading(true)
    try {
      const response = await fetch(`http://localhost:8080/api/banks/${id}`, {
        method: 'DELETE'
      })

      if (!response.ok) throw new Error('Failed to delete bank')
      
      await fetchBanks()
      setError('')
      alert('Bank deleted successfully!')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // Edit bank
  const editBank = (bank) => {
    setEditingBank(bank)
    setFormData({
      code: bank.code,
      name: bank.name,
      country: bank.country || '',
      active: bank.active
    })
    setShowForm(true)
  }

  // Reset form
  const resetForm = () => {
    setFormData({
      code: '',
      name: '',
      country: '',
      active: true
    })
    setEditingBank(null)
    setShowForm(false)
  }

  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }))
  }

  // Load banks on component mount
  useEffect(() => {
    fetchBanks()
  }, [])

  return (
    <div className="app">
      <header className="header">
        <h1>Banking CRUD System</h1>
        <p>Complete REST API for Banking Entities Management</p>
      </header>

      <main className="main">
        {error && <div className="error">{error}</div>}
        
        <div className="actions">
          <button onClick={() => setShowForm(true)} className="btn btn-primary">
            Add New Bank
          </button>
          <button onClick={fetchBanks} className="btn btn-secondary" disabled={loading}>
            Refresh
          </button>
          <button onClick={testInternalQuery} className="btn btn-info" disabled={loading}>
            Test Internal Query
          </button>
        </div>

        {showForm && (
          <div className="modal">
            <div className="modal-content">
              <h2>{editingBank ? 'Edit Bank' : 'Add New Bank'}</h2>
              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label htmlFor="code">Code *</label>
                  <input
                    type="text"
                    id="code"
                    name="code"
                    value={formData.code}
                    onChange={handleInputChange}
                    required
                    disabled={editingBank} // Code cannot be changed when editing
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="name">Name *</label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="country">Country</label>
                  <input
                    type="text"
                    id="country"
                    name="country"
                    value={formData.country}
                    onChange={handleInputChange}
                  />
                </div>
                
                <div className="form-group">
                  <label>
                    <input
                      type="checkbox"
                      name="active"
                      checked={formData.active}
                      onChange={handleInputChange}
                    />
                    Active
                  </label>
                </div>
                
                <div className="form-actions">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Saving...' : (editingBank ? 'Update' : 'Create')}
                  </button>
                  <button type="button" onClick={resetForm} className="btn btn-secondary">
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="banks-table">
          <h2>Banks List ({banks.length})</h2>
          
          {loading ? (
            <div className="loading">Loading...</div>
          ) : banks.length === 0 ? (
            <div className="no-data">No banks found. Create your first bank!</div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Country</th>
                  <th>Active</th>
                  <th>Creation Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {banks.map(bank => (
                  <tr key={bank.id}>
                    <td>{bank.id}</td>
                    <td>{bank.code}</td>
                    <td>{bank.name}</td>
                    <td>{bank.country || '-'}</td>
                    <td>
                      <span className={`status ${bank.active ? 'active' : 'inactive'}`}>
                        {bank.active ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td>{new Date(bank.creationDate).toLocaleString()}</td>
                    <td>
                      <button onClick={() => editBank(bank)} className="btn btn-sm btn-secondary">
                        Edit
                      </button>
                      <button onClick={() => deleteBank(bank.id)} className="btn btn-sm btn-danger">
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  )
}

export default App
